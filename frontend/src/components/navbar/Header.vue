<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { useUser } from '@/composables/useUser';
import MobileNavSidebar from '@/components/navbar/MobileNavSidebar.vue';
import AppNavLink from '@/components/navbar/AppNavLink.vue';
import UserActionsDropdown from '@/components/navbar/UserActionsDropdown.vue';
import Logo from '@/components/Logo.vue';
import { navbarLinks } from '@/components/navbar/links';

const { isSignedIn } = useUser();
</script>

<template>
  <header
    class="sticky top-0 z-50 flex h-16 items-center border border-transparent bg-neutral-950 px-6 text-white"
  >
    <MobileNavSidebar />

    <nav class="ml-4 flex flex-grow justify-between sm:ml-0">
      <ul class="flex items-center gap-6">
        <RouterLink to="/">
          <Logo class="h-8 w-8" />
        </RouterLink>

        <li class="hidden sm:block" v-for="{ to, name } in navbarLinks" :key="`navbar-${name}`">
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
