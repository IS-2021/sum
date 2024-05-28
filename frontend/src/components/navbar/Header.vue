<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { useUser } from '@/composables/useUser';
import DropdownMenu from '@/components/navbar/DropdownMenu.vue';
import AppNavLink from '@/components/navbar/AppNavLink.vue';
import UserActionsDropdown from '@/components/navbar/UserActionsDropdown.vue';
import type { AppRoutes } from '@/lib/router';

const { isSignedIn } = useUser();

const links: { name: string; to: AppRoutes }[] = [
  { name: 'Home', to: '/' },
  { name: 'Restaurants', to: '/restaurants' },
  { name: 'Favourites', to: '/favourites' },
  { name: 'Bookings', to: '/bookings' },
];
</script>

<template>
  <header
    class="h-16 w-full top-0 border border-transparent bg-neutral-950 sticky flex items-center px-6 z-50"
  >
    <nav class="flex-grow">
      <ul class="flex items-center gap-6 text-white">
        <li>
          <DropdownMenu />
        </li>

        <li>
          <div class="h-8 w-8">
            <RouterLink to="/">
              <img src="../../assets/logo.svg" alt="Logo" />
            </RouterLink>
          </div>
        </li>
        <li class="hidden sm:block" v-for="{ to, name } in links" :key="`navbar-${name}`">
          <AppNavLink :to="to">{{ name }}</AppNavLink>
        </li>

        <li class="flex-grow"></li>

        <li class="flex gap-2">
          <UserActionsDropdown v-if="isSignedIn" />

          <Button v-else as-child>
            <RouterLink to="/sign-in">Sign in</RouterLink>
          </Button>
        </li>
      </ul>
    </nav>
  </header>
</template>
