import { createApp } from 'vue';
import { createPinia } from 'pinia';

import './assets/main.css';
import '@fontsource-variable/inter';

import App from './App.vue';
import router from './router';
import { createHead } from '@unhead/vue';
import { VueQueryPlugin } from '@tanstack/vue-query';
import { queryClientConfig } from '@/lib/tanstack-query';

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(VueQueryPlugin, {
  queryClientConfig,
});

const head = createHead();
app.use(head);

app.mount('#app');
