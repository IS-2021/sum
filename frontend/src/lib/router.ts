import type { Router } from 'vue-router/auto';

export type AppRoutes = Parameters<Router['push']>[0];
