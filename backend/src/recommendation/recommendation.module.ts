import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { MovieDatabase, MovieDatabaseSchema } from 'src/schema/database.schema';
import {
  MoviesRecommendations,
  MoviesRecommendationsSchema,
} from 'src/schema/movies-recommendation.schema';
import { Movie, MovieSchema } from 'src/schema/movies.schema';
import { RecommendationTask } from 'src/task/recommendation.service';
import { Favorites, FavoritesSchema } from '../schema/favorites.schema';
import { TasksService } from '../task/task.service';
import { RecommendationsController } from './recommendation.controller';
import { RecommendationService } from './recommendation.service';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: MovieDatabase.name, schema: MovieDatabaseSchema },
      { name: Movie.name, schema: MovieSchema },
      { name: Favorites.name, schema: FavoritesSchema },
      { name: MoviesRecommendations.name, schema: MoviesRecommendationsSchema },
      { name: Favorites.name, schema: FavoritesSchema },
    ]),
  ],
  controllers: [RecommendationsController],
  providers: [RecommendationService, TasksService, RecommendationTask],
})
export class RecommendationModule {}
