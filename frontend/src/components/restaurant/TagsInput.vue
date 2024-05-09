<script setup lang="ts">
import { ref } from 'vue';
import { TagsInputItem, TagsInputItemDelete, TagsInputItemText, TagsInputRoot } from 'radix-vue';
import { X } from 'lucide-vue-next';

const modelValue = ref(['Apple', 'Banana']);
</script>

<template>
  <TagsInputRoot v-model="modelValue" class="flex gap-2 items-start w-full max-w-[480px] flex-wrap">
    <TagsInputItem
      v-for="item in modelValue"
      :key="item"
      :value="item"
      class="text-neutral-950 bg-white flex shadow-md items-center justify-center gap-2 bg-green8 aria-[current=true]:bg-green9 rounded-xl p-1"
    >
      <TagsInputItemText class="text-sm pl-1" />
      <TagsInputItemDelete class="p-0.5 rounded bg-transparent hover:bg-blackA4">
        <X class="w-4 h-4 text-neutral-950" />
      </TagsInputItemDelete>
    </TagsInputItem>
  </TagsInputRoot>
</template>
