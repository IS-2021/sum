<script setup lang="ts">
import { ref } from 'vue';
import NavLink from '@/components/(manage)/common/NavLink.vue';

import Label from '@/components/ui/label/Label.vue';
import Input from '@/components/ui/input/Input.vue';
import Button from '@/components/ui/button/Button.vue';

import { postMeals } from '@/lib/api/meals/meals';
import type { Uuid } from '@/lib/api-model';

const props = defineProps<{
  userId: Uuid;
}>();

const mealName = ref('');
const mealDescription = ref('');
let mealUpdateLink = ref('');

async function addNewMeal() {
  const response = await postMeals({
    description: mealDescription.value,
    name: mealName.value,
    restaurantId: props.userId,
  });
  mealUpdateLink.value = `/manage/mealUpdate/${response.data.mealId}`;
}
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-6">Add Meal</h1>
  <p class="mb-16">After you add your meal you will be willing to add ingredients to it</p>
  <div class="max-w-screen-md w-full my-4">
    <Label>Name</Label>
    <Input placeholder="meal name..." v-model="mealName" />
  </div>
  <div class="max-w-screen-md w-full my-4 mb-8">
    <Label>Type</Label>
    <Input placeholder="meal description..." v-model="mealDescription" />
  </div>
  <Button v-if="mealName === '' || mealDescription === ''" disabled class="bg-opacity-70"
    >Add meal</Button
  >
  <NavLink v-else :to="mealUpdateLink" class="bg-inherit">
    <Button @click="addNewMeal()">Add meal</Button>
  </NavLink>
</template>
