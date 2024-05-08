<script setup lang="ts">
import { TagsInputItem, TagsInputItemDelete, TagsInputItemText, TagsInputRoot } from 'radix-vue';
import { X } from 'lucide-vue-next';
import type { IngredientDTO } from '@/lib/api-model';
import { ref } from 'vue';

const props = defineProps<{
  unwantedIngredients: IngredientDTO[];
}>();

const emit = defineEmits<{
  (e: 'filterChange', unwantedIngredients: IngredientDTO[]): void;
}>();

const unwantedIngredients = ref(props.unwantedIngredients);

function deleteFilter(item: IngredientDTO) {
  const index = unwantedIngredients.value.indexOf(item);
  unwantedIngredients.value.splice(index, 1);
  emit('filterChange', unwantedIngredients.value);
}
</script>

<template>
  <TagsInputRoot class="flex gap-2 items-start w-full max-w-[480px] flex-wrap">
    <TagsInputItem
      v-for="item in unwantedIngredients"
      :key="item.id"
      :value="item.name"
      class="text-neutral-950 bg-white flex shadow-md items-center justify-center gap-2 bg-green8 aria-[current=true]:bg-green9 rounded-xl p-1"
    >
      <TagsInputItemText class="text-sm pl-1" />
      <TagsInputItemDelete class="p-0.5 rounded bg-transparent hover:bg-blackA4">
        <X class="w-4 h-4 text-neutral-950" @click="deleteFilter(item)" />
      </TagsInputItemDelete>
    </TagsInputItem>
  </TagsInputRoot>
</template>
