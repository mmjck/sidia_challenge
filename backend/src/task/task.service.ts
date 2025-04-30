import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Cron, CronExpression } from '@nestjs/schedule';
import { Model } from 'mongoose';
import { MovieDatabase } from 'src/schema/database.schema';
import { Favorites } from 'src/schema/favorites.schema';
import { MoviesRecommendations } from 'src/schema/movies-recommendation.schema';
import { Movie } from 'src/schema/movies.schema';
import { RecommendationTask } from './recommendation.service';

@Injectable()
export class TasksService {
  constructor(
    @InjectModel(Movie.name) private database: Model<Movie>,

    @InjectModel(MovieDatabase.name) private movieModel: Model<MovieDatabase>,

    @InjectModel(MoviesRecommendations.name)
    private recommendations: Model<MoviesRecommendations>,

    @InjectModel(Favorites.name)
    private favorite: Model<Favorites>,

    private readonly recommendation: RecommendationTask,
  ) {}

  private readonly logger = new Logger(TasksService.name);

  @Cron(CronExpression.EVERY_10_SECONDS)
  async updateDatabase() {
    //   this.logger.debug('Update database');
    //   const movies = await this.database.aggregate([
    //     { $match: { vote_average: { $gt: 0 }, vote_count: { $gt: 100 } } },
    //     { $sample: { size: 1000 } },
    //   ]);
    //   this.logger.debug(`Fetched ${movies.length} random movies.`);
    //   await this.movieModel.collection.drop();
    //   this.logger.debug('Dropped current movie collection.');
    //   await this.movieModel.createCollection();
    //   await this.movieModel.insertMany(movies);
    //   this.logger.debug('Created new movie collection with random movies.');
  }

  @Cron(CronExpression.EVERY_DAY_AT_MIDNIGHT)
  async updateRecommendations() {
    await this.recommendations.collection.drop();
    this.logger.debug('Dropped current movie collection.');
    await this.recommendations.createCollection();

    const favoritesMovies = await this.favorite
      .find()
      .sort({ createdAt: -1 })
      .limit(5);

    // insert ramdon movies
    if (favoritesMovies.length == 0) {
      const bestMovies = await this.movieModel
        .find()
        .sort({ vote_count: -1 })
        .limit(5);

      const movieIds = bestMovies.map((fav) => fav.id);

      const data = await this.recommendation.recommendMoviesFromList(movieIds);

      await this.recommendations.insertMany(data);
      return;
    }

    // insert movies based on favorites movies
    if (favoritesMovies.length != 0) {
      const movieIds = favoritesMovies.map((fav) => fav.movie_id);

      const data = await this.recommendation.recommendMoviesFromList(movieIds);

      await this.recommendations.insertMany(data);
      return;
    }
  }
}
