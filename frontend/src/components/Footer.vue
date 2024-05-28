<script setup lang="ts">
import type { AppRoutes } from '@/lib/router.js';
import Logo from '@/components/Logo.vue';

const links: { name: string; to: AppRoutes }[] = [
  { name: 'Home', to: '/' },
  { name: 'Restaurants', to: '/restaurants' },
  { name: 'Favourites', to: '/favourites' },
  { name: 'Bookings', to: '/bookings' },
];
</script>

<template>
  <footer class="bottom-0 bg-neutral-800 flex justify-between p-10 text-neutral-50">
    <div>
      <Logo class="w-16 h-16 mb-3" />
      <p class="text-xs text-neutral-400">&copy; 2024</p>
    </div>
    <div class="flex flex-col w-32">
      <nav>
        <ul class="space-y-3">
          <li v-for="{ to, name } in links" :key="`footer-${name}`" class="text-right">
            <RouterLink :to="to">{{ name }}</RouterLink>
          </li>
        </ul>
      </nav>
    </div>
  </footer>
</template>
