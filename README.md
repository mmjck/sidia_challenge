<h1 align = "center">
  Movies TMDB
</h1>



<h2 align = "flex-start">
  Desafio técnico SIDIA
</h2>

Aplicação para listagem de filmes com base no banco de dados disponibilizado em 
[DATASET](https://www.kaggle.com/datasets/asaniczka/tmdb-movies-dataset-2023-930k-movies)


## Requisitos
- NodeJs
- Docker
- Android Studio

## Tecnologias utilizadas
 - NestJs
 - MongoDB
 - Kotlin
 - MVVM
 - Hilt
 - Retrofit e OkHttp <
 - Gson 
 - Glide 



## Como rodar a aplicação

Clone este repositório
```bash
git clone https://github.com/mmjck/sidia_challenge.git
```

Iniciar Container Mongo

```
cd backend/local && docker compose up -d
```

Popular dados

```
cd database && node index.js
```

Depois disso, abra o Android Studio, vá para Open (Abrir) e abra a pasta Movies.
