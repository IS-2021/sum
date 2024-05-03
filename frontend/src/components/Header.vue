<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { useUser } from '@/composables/useUser';
import DropdownMenu from '@/components/DropdownMenu.vue';

const { user, isSignedIn, signOut } = useUser();
</script>

<template>
  <header
    class="h-16 w-full top-0 border border-transparent bg-zinc-800 sticky flex items-center px-6 z-50"
  >
    <nav class="flex-grow">
      <ul class="flex items-center gap-6">
        <li>
          <DropdownMenu />
        </li>
        <li>
          <div class="h-8 w-8">
            <RouterLink to="/">
              <img src="@/assets/logo.svg" alt="Logo" />
            </RouterLink>
          </div>
        </li>
        <li class="hidden sm:block">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="hidden sm:block">
          <RouterLink to="/restaurants">Restaurants</RouterLink>
        </li>
        <li class="flex-grow"></li>
        <li class="flex gap-2">
          <Button v-if="isSignedIn" @click="signOut">Log out</Button>
          <Button v-else as-child>
            <RouterLink to="/sign-in">Sign in</RouterLink>
          </Button>
        </li>
      </ul>
    </nav>
  </header>
</template>
