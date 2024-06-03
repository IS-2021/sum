<script setup lang="ts">
import { Search } from 'lucide-vue-next';
import { Input } from '@/components/ui/input';
import type { HTMLAttributes } from 'vue';
import { useVModel } from '@vueuse/core';
import { cn } from '@/lib/utils';

const props = defineProps<{
  defaultValue?: string | number;
  modelValue?: string | number;
  class?: HTMLAttributes['class'];
}>();

const emits = defineEmits<{
  (e: 'update:modelValue', payload: string | number): void;
}>();

const modelValue = useVModel(props, 'modelValue', emits, {
  passive: true,
  defaultValue: props.defaultValue,
});
</script>

<template>
  <div class="relative w-full max-w-sm items-center">
    <Input
      v-model="modelValue"
      id="search"
      type="text"
      placeholder="Search..."
      :class="cn('pl-10', props.class)"
    />
    <span class="absolute inset-y-0 start-0 flex items-center justify-center px-2">
      <Search class="text-muted-foreground size-5" />
    </span>
  </div>
</template>
