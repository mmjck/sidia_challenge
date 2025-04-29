const fs = require('fs-extra');
const path = require('path');
const axios = require('axios');
const unzipper = require('unzipper');
const csv = require('csv-parser');
const mongoose = require('mongoose');

// Setup mongoose schema
const movieSchema = new mongoose.Schema({
    tmdb_id: Number,
    title: String,
    original_title: String,
    overview: String,
    release_date: Date,
    budget: Number,
    revenue: Number,
    runtime: Number,
    vote_average: Number,
    vote_count: Number,
    popularity: Number,
    status: String,
    genres: Array,
    spoken_languages: Array,
    path: String,
    production_companies: Array,

    created_at: { type: Date, default: Date.now }
});
const Movie = mongoose.model('movies', movieSchema);

const DATASET_URL = ' https://www.kaggle.com/api/v1/datasets/download/asaniczka/tmdb-movies-dataset-2023-930k-movies';

async function downloadAndExtract(zipPath, extractTo) {
    console.log('⬇️  Baixando dataset...');
    const response = await axios({
        url: DATASET_URL,
        method: 'GET',
        responseType: 'stream',
        headers: {
            'User-Agent': 'Mozilla/5.0',
        },
    });

    const writer = fs.createWriteStream(zipPath);
    response.data.pipe(writer);

    await new Promise((resolve, reject) => {
        writer.on('finish', resolve);
        writer.on('error', reject);
    });

    console.log('📦 Extraindo...');
    await fs.createReadStream(zipPath)
        .pipe(unzipper.Extract({ path: extractTo }))
        .promise();
}

async function importCSVToMongo(csvPath) {
    console.log('Importando dados para MongoDB...');
    await mongoose.connect('mongodb://admin:123@localhost:27017/tmdb?authSource=admin');

    const movies = [];
    let count = 0;

    return new Promise((resolve, reject) => {
        fs.createReadStream(csvPath)
            .pipe(csv())
            .on('data', (row) => {
                const movie = {
                    tmdb_id: parseInt(row.id),
                    title: row.title,
                    original_title: row.original_title,
                    overview: row.overview,
                    release_date: row.release_date ? new Date(row.release_date) : null,
                    budget: parseInt(row.budget),
                    revenue: parseInt(row.revenue),
                    runtime: parseInt(row.runtime),
                    vote_average: parseFloat(row.vote_average),
                    vote_count: parseInt(row.vote_count),
                    popularity: parseFloat(row.popularity),
                    status: row.status,
                    path: row.backdrop_path,
                    genres: row.genres ? row.genres.split(',').map(g => ({ name: g.trim() })) : [],
                    spoken_languages: row.spoken_languages ? row.spoken_languages.split(',').map(l => ({ name: l.trim() })) : [],
                    production_companies: row.production_companies ? row.production_companies.split(',').map(c => ({ name: c.trim() })) : [],
                };

                console.log(movie);


                movies.push(movie);
                count++;

                if (movies.length === 1000) {
                    Movie.insertMany(movies.splice(0, 1000)).catch(console.error);
                }

                return;
            })
            .on('end', async () => {
                if (movies.length > 0) {
                    await Movie.insertMany(movies);
                }
                console.log(`Importado ${count} filmes.`);
                resolve();
            })
            .on('error', reject);
    });
}

async function main() {
    const zipPath = path.join(__dirname, 'tmdb.zip');
    const extractTo = path.join(__dirname, 'tmdb');
    const csvFile = path.join(extractTo, 'TMDB_MOVIES.csv');

    try {
        await downloadAndExtract(zipPath, extractTo);
        await importCSVToMongo(csvFile);
        console.log('Concluído!');
    } catch (err) {
        console.error('Erro:', err);
    } finally {
        mongoose.disconnect();
    }
}

main();
