<script setup lang="ts">
import { Button } from '@/components/ui/button';
import { Sheet, SheetContent, SheetHeader, SheetTitle, SheetTrigger } from '@/components/ui/sheet';
import { MenuIcon } from 'lucide-vue-next';
import Logo from '@/components/Logo.vue';
import AppNavLink from '@/components/navbar/AppNavLink.vue';
import { navbarLinks } from '@/components/navbar/links';
</script>

<template>
  <Sheet>
    <SheetTrigger as-child class="sm:hidden">
      <Button variant="ghost" size="icon" class="hover:bg-secondary">
        <MenuIcon />
      </Button>
    </SheetTrigger>

    <SheetContent side="left" class="border-neutral-700 bg-neutral-950 text-neutral-50">
      <SheetHeader class="mb-10">
        <SheetTitle>
          <RouterLink to="/" class="flex items-center gap-4 text-primary">
            <Logo /> <span class="text-2xl tracking-tight">FoodGood</span>
          </RouterLink>
        </SheetTitle>
      </SheetHeader>

      <nav>
        <ul class="space-y-3">
          <li v-for="{ to, name } in navbarLinks" :key="`navbar-${name}`">
            <AppNavLink :to="to">{{ name }}</AppNavLink>
          </li>
        </ul>
      </nav>
    </SheetContent>
  </Sheet>
</template>
