import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { MovieDatabase } from 'src/schema/database.schema';
import { Favorites } from 'src/schema/favorites.schema';
import { MoviesRecommendations } from 'src/schema/movies-recommendation.schema';
import { Movie } from 'src/schema/movies.schema';
import * as faiss from 'faiss-node';

@Injectable()
export class RecommendationService {
  constructor(
    @InjectModel(MovieDatabase.name)
    private movieDB: Model<MovieDatabase>,

    @InjectModel(Movie.name)
    private database: Model<Movie>,

    @InjectModel(MoviesRecommendations.name)
    private recommendations: Model<MoviesRecommendations>,

    @InjectModel(Favorites.name)
    private favorite: Model<Favorites>,
  ) {}

  async get(page: number, limit: number) {
    const skip = (page - 1) * limit;

    const favoritesMovives = await this.favorite.find().skip(skip).limit(limit);

    if (favoritesMovives.length != 0) {
      return;
    }

    const data = await this.movieDB
      .find()
      .sort({ voteCount: -1 })
      .skip(skip)
      .limit(limit);

    console.log(data);

    const total = await this.movieDB.countDocuments();

    return {
      data,
      pagination: {
        total,
        page,
        limit,
        pages: Math.ceil(total / limit),
      },
    };
  }

  async getRecRelatedTo(page: number, limit: number, movies: string[]) {
    const data = await this.recommendMoviesFromList(movies);
    const total = data.length;

    return {
      data,
      pagination: {
        total,
        page,
        limit,
        pages: Math.ceil(total / limit),
      },
    };
  }

  private genreList = ['Action', 'Adventure', 'Drama', 'Comedy'];
  private languageList = ['English', 'Spanish', 'French'];

  private movieDatabaseToVector(movie: MovieDatabase): number[] {
    const vector: number[] = [];

    this.genreList.forEach((genre) => {
      vector.push(movie.genres?.some((g) => g.name === genre) ? 1 : 0);
    });

    this.languageList.forEach((lang) => {
      vector.push(movie.spoken_languages?.some((l) => l.name === lang) ? 1 : 0);
    });

    vector.push(Math.min((movie.popularity || 0) / 10, 1));
    vector.push(Math.min((movie.vote_average || 0) / 10, 1));
    vector.push(Math.min((movie.runtime || 0) / 180, 1));

    return vector;
  }

  private averageVectors(vectors: number[][]): number[] {
    const dimension = vectors[0].length;
    const avgVector = new Array(dimension).fill(0);

    vectors.forEach((vector) => {
      vector.forEach((value, idx) => {
        avgVector[idx] += value;
      });
    });

    return avgVector.map((sum) => sum / vectors.length);
  }

  async recommendMoviesFromList(ids: string[], k: number = 5) {
    const movies = await this.movieDB.find().lean();

    if (!movies.length) {
      throw new Error('Nenhum filme encontrado no banco.');
    }

    const vectors: number[][] = movies.map((movie) =>
      this.movieDatabaseToVector(movie),
    );

    const flatVectors: number[] = vectors.flat();

    const dimension = vectors[0].length;
    const index = new faiss.IndexFlatL2(dimension);

    index.add(flatVectors);

    const targetMovies = movies.filter((m: MovieDatabase) =>
      ids.includes(m._id.toString()),
    );

    if (!targetMovies.length) {
      throw new Error('Nenhum filme da lista encontrado!');
    }

    const targetVectors = targetMovies.map((movie) =>
      this.movieDatabaseToVector(movie as MovieDatabase),
    );

    const avgVectorArray = this.averageVectors(targetVectors);
    const result = index.search(avgVectorArray, k + targetMovies.length);

    const idToMovie: Map<number, MovieDatabase> = new Map();
    movies.forEach((movie, idx) => {
      idToMovie.set(idx, movie as MovieDatabase);
    });

    const recommendations = result.labels
      .filter((label) => {
        const movie = idToMovie.get(label);
        return movie && !ids.includes(movie._id.toString());
      })
      .slice(0, k)
      .map((label) => idToMovie.get(label));

    return recommendations;
  }
}
