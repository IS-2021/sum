import { createApp } from 'vue';
import { createPinia } from 'pinia';

import './assets/main.css';

import App from './App.vue';
import router from './router';
import { createHead } from '@unhead/vue';

const app = createApp(App);

app.use(createPinia());
app.use(router);

const head = createHead();
app.use(head);

app.mount('#app');
