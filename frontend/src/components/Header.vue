<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { postLogout } from '@/lib/api/auth/auth';
</script>

<template>
  <header class="h-16 w-full top-0 border border-transparent bg-zinc-800 sticky flex items-center px-6 z-50">
    <nav class="flex-grow">
      <ul class="flex items-center gap-6">
        <li>
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/restaurants">Restaurants</RouterLink>
        </li>
        <li class="flex-grow"></li>
        <li class="flex gap-2">
          <Button @click="postLogout">Log out</Button>
          <Button as-child>
            <RouterLink to="/sign-in">Sign in</RouterLink>
          </Button>
        </li>
      </ul>
    </nav>
  </header>
</template>
