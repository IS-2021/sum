import { defineConfig } from 'orval';

export default defineConfig({
  api: {
    input: {
      target: '../Sumatywny.yaml',
    },
    output: {
      mode: 'tags-split',
      target: './src/lib/api',
      schemas: './src/lib/api-model',
      client: 'vue-query',
      baseUrl: 'http://localhost:9090/',
      prettier: true,
    },
  },
});
