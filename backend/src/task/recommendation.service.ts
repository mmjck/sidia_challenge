import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import * as faiss from 'faiss-node';
import { Model } from 'mongoose';
import { MovieDatabase } from 'src/schema/database.schema';

@Injectable()
export class RecommendationTask {
  constructor(
    @InjectModel(MovieDatabase.name)
    private movieDB: Model<MovieDatabase>,
  ) {}

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

  async recommendMoviesFromList(ids: string[], k: number = 10) {
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
