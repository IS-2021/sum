<script setup lang="ts">
import type { MealDTO, Uuid } from '@/lib/api-model';
import AddIngredientPopup from '@/components/(manage)/meals/AddIngredientPopup.vue';
import { Button } from '@/components/ui/button';
import { useGetIngredients } from '@/lib/api/ingredients/ingredients';
import { computed } from 'vue';
import { CircleCheckBigIcon, PlusIcon } from 'lucide-vue-next';
import { cn } from '@/lib/utils';
import { recreateMeal } from '@/components/(manage)/meals/api';
import { useRouter } from 'vue-router/auto';
import { toast } from 'vue-sonner';

const props = defineProps<{
  meal: MealDTO;
  userId: Uuid;
}>();

const router = useRouter();
const { data } = useGetIngredients();

const allIngredients = computed(() => data.value?.data ?? []);

const pickedIngredients = ref<IngredientDTO[]>(props.meal.ingredients ?? []);
const pickedIngredientsIds = computed(() => {
  const ids = pickedIngredients.value.map((i) => i.ingredientId);
  return new Set(ids);
});

function isPicked(ingredient: string) {
  return pickedIngredientsIds.value.has(ingredient);
}

function toggleIngredientPick(ingredient: IngredientDTO) {
  if (pickedIngredientsIds.value.has(ingredient.ingredientId)) {
    pickedIngredients.value = pickedIngredients.value.filter(
      (i) => i.ingredientId !== ingredient.ingredientId,
    );
  } else {
    pickedIngredients.value = [...pickedIngredients.value, ingredient];
  }
}

// Since meals are immutable, we need to recreate the meal with the new ingredients
async function handleEditMeal() {
  const res = await recreateMeal({
    ...props.meal,
    ingredients: pickedIngredients.value,
  });

  if (res.status === 200) {
    await router.replace(`/manage/meals/edit/${res.data.mealId}`);
    location.reload();
  } else {
    toast.error('Failed to update meal');
    console.error('recreateMeal:postMeals', res);
  }
}
</script>

<template>
  <header class="flex justify-between">
    <h1 class="text-2xl font-semibold tracking-tight mb-10">Add ingredients to your meal</h1>
    <AddIngredientPopup :mealId="meal.mealId" :userId="props.userId" />
  </header>

  <article class="bg-neutral-200 border p-4 rounded-md">
    <div class="flex gap-1 mb-2">
      <p><span class="font-bold">Name:</span> {{ meal.name }}</p>
    </div>
    <div class="flex gap-1">
      <p><span class="font-bold">Description:</span> {{ meal.description }}</p>
    </div>

    <section class="mt-10">
      <Button @click="handleEditMeal">Save</Button>

      <h2 class="text-lg font-semibold mt-4">Ingredients</h2>
      <ul class="space-y-2 mt-3">
        <li v-for="ingredient in allIngredients" :key="ingredient.ingredientId">
          <div
            :class="
              cn(
                'flex items-center gap-2 transition-colors',
                isPicked(ingredient.ingredientId) && 'text-primary',
              )
            "
          >
            <Button size="icon" variant="ghost" @click="toggleIngredientPick(ingredient)">
              <CircleCheckBigIcon class="h-4 w-4" v-if="isPicked(ingredient.ingredientId)" />
              <PlusIcon class="h-4 w-4" v-else />
            </Button>

            <p>
              {{ ingredient.name }}
            </p>
          </div>
        </li>
      </ul>
    </section>
  </article>
</template>
