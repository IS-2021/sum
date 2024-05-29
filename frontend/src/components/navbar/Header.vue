<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { useUser } from '@/composables/useUser';
import DropdownMenu from '@/components/navbar/DropdownMenu.vue';
import AppNavLink from '@/components/navbar/AppNavLink.vue';
import UserActionsDropdown from '@/components/navbar/UserActionsDropdown.vue';
import type { AppRoutes } from '@/lib/router';
import Logo from '@/components/Logo.vue';

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
    class="sticky top-0 z-50 flex h-16 w-full items-center border border-transparent bg-neutral-950 px-6 text-white"
  >
    <DropdownMenu />

    <nav class="ml-4 flex flex-grow justify-between sm:ml-0">
      <ul class="flex items-center gap-6">
        <RouterLink to="/">
          <Logo class="h-8 w-8" />
        </RouterLink>

        <li class="hidden sm:block" v-for="{ to, name } in links" :key="`navbar-${name}`">
          <AppNavLink :to="to">{{ name }}</AppNavLink>
        </li>
      </ul>

      <Button v-if="!isSignedIn" as-child>
        <RouterLink to="/sign-in">Sign in</RouterLink>
      </Button>
    </nav>

    <UserActionsDropdown v-if="isSignedIn" />
  </header>
</template>
