import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { Favorites, FavoritesSchema } from '../schema/favorites.schema';
import { MoviesController } from './movies.controller';
import { MoviesService } from './movies.service';
import { Movie, MovieSchema } from 'src/schema/movies.schema';
import { MovieDatabase, MovieDatabaseSchema } from 'src/schema/database.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Movie.name, schema: MovieSchema },
      { name: MovieDatabase.name, schema: MovieDatabaseSchema },
      { name: Favorites.name, schema: FavoritesSchema },
    ]),
  ],
  controllers: [MoviesController],
  providers: [MoviesService],
})
export class MoviesModule {}
