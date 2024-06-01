<script setup lang="ts">
import { Sheet, SheetContent, SheetTrigger } from '@/components/ui/sheet';
import { Button } from '@/components/ui/button';
import { MenuIcon } from 'lucide-vue-next';
import Navbar from '@/components/(admin)/common/Navbar.vue';
</script>

<template>
  <Sheet>
    <SheetTrigger as-child>
      <Button variant="outline" size="icon">
        <MenuIcon />
      </Button>
    </SheetTrigger>
    <SheetContent side="left" class="max-w-80">
      <Navbar />
    </SheetContent>
  </Sheet>
</template>
