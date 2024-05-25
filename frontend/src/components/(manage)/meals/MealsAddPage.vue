<script setup lang="ts">
import { ref } from 'vue';

import Label from '@/components/ui/label/Label.vue';
import Input from '@/components/ui/input/Input.vue';

import AddIngredientPopup from './AddIngredientPopup.vue';
import { useUser } from '@/composables/useUser';

const { user } = useUser();

const mealName = ref('');
const mealId = ref('');
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-10">Add Meal</h1>
  <div class="max-w-screen-md w-full my-4">
    <Label>Name</Label>
    <Input placeholder="meal name..." v-model="mealName" />
  </div>
  <div class="max-w-screen-md w-full my-4 mb-8">
    <Label>Type</Label>
    <Input placeholder="meal type..." v-model="mealName" />
  </div>
  <AddIngredientPopup v-if="user" :mealId="mealId" :userId="user.id" />
</template>
