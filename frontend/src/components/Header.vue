<script setup lang="ts">
import { Button } from '@/components/ui/button';
</script>

<template>
  <header
    class="h-16 w-full top-0 border border-transparent bg-zinc-800 sticky flex items-center px-6"
  >
    <nav class="flex-grow">
      <ul class="flex items-center gap-6">
        <li>
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/about">About</RouterLink>
        </li>
        <li class="flex-grow"></li>
        <li>
          <Button as-child>
            <RouterLink to="/sign-in">Sign in</RouterLink>
          </Button>
        </li>
      </ul>
    </nav>
  </header>
</template>
