<route lang="yaml">
meta:
  layout: manage
</route>

<script setup lang="ts">
import { useUser } from '@/composables/useUser';
import { useRoute } from 'vue-router/auto';
import { useGetMealsId } from '@/lib/api/meals/meals';
import { computed, unref } from 'vue';

import AddIngredientsComponent from '@/components/(manage)/meals/AddIngredientsComponent.vue';

const route = useRoute('/manage/mealUpdate/[id]');
const mealId = route.params.id;

const { data } = useGetMealsId(mealId);
const meal = computed(() => unref(data)?.data);

const { user } = useUser();
</script>

<template>
  <AddIngredientsComponent v-if="meal && user" :meal="meal" :userId="user.id" />
</template>
