<script setup lang="ts">
import NavLink from '@/components/(manage)/common/NavLink.vue';
import Logo from '@/components/Logo.vue';
import { UsersRoundIcon, ChefHatIcon } from 'lucide-vue-next';
</script>

<template>
  <aside class="w-full max-h-svh h-full px-6 py-5 sticky top-0 flex flex-col">
    <RouterLink to="/admin/restaurants">
      <Logo class="mb-8" />
    </RouterLink>

    <div class="flex flex-col justify-between flex-grow">
      <nav>
        <ul class="space-y-2">
          <li>
            <NavLink to="/admin/restaurants">
              <ChefHatIcon />
              Restaurants
            </NavLink>
          </li>
          <li>
            <NavLink to="/admin/users">
              <UsersRoundIcon />
              Users
            </NavLink>
          </li>
        </ul>
      </nav>
    </div>
  </aside>
</template>
