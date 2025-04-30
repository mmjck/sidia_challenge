import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { MovieDatabase } from 'src/schema/database.schema';
import { Favorites } from '../schema/favorites.schema';

@Injectable()
export class MoviesService {
  constructor(
    @InjectModel(MovieDatabase.name) private database: Model<MovieDatabase>,
    @InjectModel(Favorites.name) private favoritesModel: Model<Favorites>,
  ) {}

  async findAll(page: number, limit: number) {
    const skip = (page - 1) * limit;
    const data = await this.database.find().skip(skip).limit(limit);
    const total = await this.database.countDocuments();
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

  async findAllFavorites(page: number, limit: number) {
    const skip = (page - 1) * limit;
    const data = await this.favoritesModel
      .find()
      .sort({ createdAt: -1 })
      .skip(skip)
      .limit(limit);

    const total = await this.favoritesModel.countDocuments();
    console.log('====================================');
    console.log(data);
    console.log('====================================');
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

  async findOne(id: string) {
    const movie = await this.database.findById(id);
    if (!movie) throw new NotFoundException('Movie not found');

    const existing = await this.favoritesModel.findOne({
      movie_id: movie.id,
    });

    const movieObject = movie.toObject();

    return {
      ...movieObject,
      isFavorite: existing != null,
    };
  }

  async setFavorite(movieId: string) {
    const movie = await this.database.findById(movieId);
    if (!movie) throw new NotFoundException('Movie not found');

    const existing = await this.favoritesModel.findOne({
      movie_id: movieId,
    });

    if (existing) {
      await this.favoritesModel.deleteOne({
        movie_id: movieId,
      });

      return;
    }

    // Create favorite
    const favorite = new this.favoritesModel({
      movie_id: movieId,
      tmdb_id: movie.tmdb_id,
      title: movie.title,
      original_title: movie.original_title,
      overview: movie.overview,
      release_date: movie.release_date,
      budget: movie.budget,
      revenue: movie.revenue,
      runtime: movie.runtime,
      vote_average: movie.vote_average,
      vote_count: movie.vote_count,
      popularity: movie.popularity,
      status: movie.status,
      path: movie.path,
      genres: movie.genres,
      spoken_languages: movie.spoken_languages,
      production_companies: movie.production_companies,
    });

    return await favorite.save();
  }
}
