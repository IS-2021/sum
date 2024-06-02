<script setup lang="ts">
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogClose,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';

const emits = defineEmits<{
  (e: 'action-confirmed'): void;
}>();
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <slot name="trigger">
        <Button type="submit"> Open Dialog </Button>
      </slot>
    </DialogTrigger>
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Confirm action</DialogTitle>
        <DialogDescription>
          <slot name="description">
            You're about to ban this user. Are you sure you want to proceed?
          </slot>
        </DialogDescription>
      </DialogHeader>

      <DialogFooter>
        <DialogClose as-child>
          <slot name="confirmButton">
            <Button type="submit" @click="emits('action-confirmed')"> Confirm </Button>
          </slot>
        </DialogClose>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>
