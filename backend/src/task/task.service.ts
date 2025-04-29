import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Cron, CronExpression } from '@nestjs/schedule';
import { Model } from 'mongoose';
import { MovieDatabase } from 'src/schema/database.schema';
import { Movie } from 'src/schema/movies.schema';

@Injectable()
export class TasksService {
  constructor(
    @InjectModel(Movie.name) private database: Model<Movie>,
    @InjectModel(MovieDatabase.name) private movieModel: Model<MovieDatabase>,
  ) {}

  private readonly logger = new Logger(TasksService.name);

  @Cron(CronExpression.EVERY_30_MINUTES)
  async updateDatabase() {
    this.logger.debug('Update database');

    const movies = await this.database.aggregate([
      { $match: { vote_average: { $gt: 0 }, vote_count: { $gt: 100 } } },
      { $sample: { size: 1000 } },
    ]);

    this.logger.debug(`Fetched ${movies.length} random movies.`);

    await this.movieModel.collection.drop();
    this.logger.debug('Dropped current movie collection.');

    await this.movieModel.createCollection();
    await this.movieModel.insertMany(movies);

    this.logger.debug('Created new movie collection with random movies.');
  }
}
