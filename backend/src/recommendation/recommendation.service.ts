import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { MoviesRecommendations } from 'src/schema/movies-recommendation.schema';
import { RecommendationTask } from 'src/task/recommendation.service';

@Injectable()
export class RecommendationService {
  constructor(
    @InjectModel(MoviesRecommendations.name)
    private moviesRecommendationsCollection: Model<MoviesRecommendations>,

    private readonly recommendations: RecommendationTask,
  ) {}

  async get(page: number, limit: number) {
    const skip = (page - 1) * limit;
    const movies = await this.moviesRecommendationsCollection
      .find()
      .sort({ vote_count: -1 })
      .skip(skip)
      .limit(limit);
    const total = await this.moviesRecommendationsCollection.countDocuments();

    return {
      data: movies,
      pagination: {
        total,
        page,
        limit,
        pages: Math.ceil(total / limit),
      },
    };
  }

  async getRecRelatedTo(page: number, limit: number, movies: string[]) {
    const data = await this.recommendations.recommendMoviesFromList(movies);
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
}
