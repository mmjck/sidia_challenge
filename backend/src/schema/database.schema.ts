import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema()
export class MovieDatabase extends Document {
  @Prop() tmdb_id: number;
  @Prop() title: string;
  @Prop() original_title: string;
  @Prop() overview: string;
  @Prop() release_date: Date;
  @Prop() budget: number;
  @Prop() revenue: number;
  @Prop() runtime: number;
  @Prop() vote_average: number;
  @Prop() vote_count: number;
  @Prop() popularity: number;
  @Prop() status: string;
  @Prop() path: string;
  @Prop({ type: [{ name: String }] }) genres: { name: string }[];
  @Prop({ type: [{ name: String }] }) spoken_languages: { name: string }[];
  @Prop({ type: [{ name: String }] }) production_companies: { name: string }[];
}

export const MovieDatabaseSchema = SchemaFactory.createForClass(MovieDatabase);
