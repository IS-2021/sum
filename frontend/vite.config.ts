import { fileURLToPath, URL } from 'node:url';

import { defineConfig } from 'vite';
import VueRouter from 'unplugin-vue-router/vite';
import vue from '@vitejs/plugin-vue';
import VueDevTools from 'vite-plugin-vue-devtools';
import Layouts from 'vite-plugin-vue-layouts';

import tailwind from 'tailwindcss';
import autoprefixer from 'autoprefixer';

// https://vitejs.dev/config/
export default defineConfig({
  css: {
    postcss: {
      plugins: [tailwind(), autoprefixer()],
    },
  },
  plugins: [
    VueRouter({
      importMode: 'async',
      routesFolder: [
        {
          src: 'src/pages',
        },
      ],
    }),
    Layouts(),
    vue(),
    VueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
});
