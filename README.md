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
 - Retrofit e OkHttp
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

```
Execute o app pelo Android Studio
```



## Abordagem 🧠


- O banco de dados escolhido foi o MongoDB para lidar com registros com estruturas mal-definidas

- A cada 30 minutos, o backend atualiza a listagem principal de filmes

### Recomendação

- Foi escolhido um algoritmo de similaridades entre os filmes com base no Genêro, Linguagem, popularidade, média de votos e duração. 

A primeira listagem de filmes é basedas nos filmes mais avaliados. Após isso, a listagem inicial é baseada nos filmes favoritos que foram escolhidos.


Também foi adicionado a recomendação baseada em um filme específico, presente na tela de Detalhes do Filme.





<img src="https://github.com/mmjck/sidia_challenge/blob/main/Screenshot_1745974457.png" height="400px" />
<img src="https://github.com/mmjck/sidia_challenge/blob/main/Screenshot_1745974463.png" height="400px" />
<img src="https://github.com/mmjck/sidia_challenge/blob/main/Screenshot_1745974466.png" height="400px" />
