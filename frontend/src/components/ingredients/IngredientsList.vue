<script setup lang="ts">
import type { Uuid } from '@/lib/api-model';
import { useGetIngredients } from '@/lib/api/ingredients/ingredients';
import { unref, computed } from 'vue';

const props = defineProps<{
  mealId: Uuid;
}>();

const { data } = useGetIngredients({ mealId: props.mealId });
const ingredients = computed(() => unref(data)?.data);
</script>

<template>
  <div v-for="ingredient in ingredients" :key="ingredient.id">
    <span>{{ ingredient.name }}</span>
  </div>
</template>
