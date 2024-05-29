<script setup lang="ts">
import Logo from '@/components/Logo.vue';
import { navbarLinks } from '@/components/navbar/links';
</script>

<template>
  <footer class="bottom-0 flex justify-between bg-neutral-800 p-10 text-neutral-50">
    <div>
      <Logo class="mb-3 h-16 w-16" />
      <p class="text-xs text-neutral-400">&copy; 2024</p>
    </div>
    <div class="flex w-32 flex-col">
      <nav>
        <ul class="space-y-3">
          <li v-for="{ to, name } in navbarLinks" :key="`footer-${name}`" class="text-right">
            <RouterLink :to="to">{{ name }}</RouterLink>
          </li>
        </ul>
      </nav>
    </div>
  </footer>
</template>
