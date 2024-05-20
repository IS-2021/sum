<script setup lang="ts">
import { Sheet, SheetClose, SheetContent, SheetFooter, SheetTrigger } from '@/components/ui/sheet';
import { MenuIcon } from 'lucide-vue-next';
import Navbar from '@/components/(manage)/common/Navbar.vue';
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

      <SheetFooter>
        <SheetClose as-child>
          <Button type="submit"> Save changes </Button>
        </SheetClose>
      </SheetFooter>
    </SheetContent>
  </Sheet>
</template>
