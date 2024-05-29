import { createRouter, createWebHistory } from 'vue-router/auto';
import { setupLayouts } from 'virtual:generated-layouts';

export default createRouter({
  history: createWebHistory(),
  extendRoutes: (routes) => setupLayouts(routes),
  scrollBehavior: () => ({ left: 0, top: 0 }),
});
