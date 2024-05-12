import type { Router } from 'vue-router/auto';

export type AppRoutes = Parameters<Router['push']>[0];
export type AppRouteNames = Router['currentRoute']['value']['name'];
