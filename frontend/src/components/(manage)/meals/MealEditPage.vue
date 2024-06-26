<script setup lang="ts">
import type { IngredientInputDTO, MealDTO, Uuid } from '@/lib/api-model';
import { SaveIcon } from 'lucide-vue-next';
import { Button } from '@/components/ui/button';
import { postIngredients, useGetIngredients } from '@/lib/api/ingredients/ingredients';
import { computed } from 'vue';
import { CircleCheckBigIcon, PlusIcon } from 'lucide-vue-next';
import { cn } from '@/lib/utils';
import { addIngredientsToMeal, recreateMeal } from '@/components/(manage)/meals/api';
import { useRouter } from 'vue-router/auto';
import { useIngredientEdit } from '@/components/(manage)/meals/useIngredientEdit';
import { toast } from 'vue-sonner';
import AddIngredientDialog from '@/components/(manage)/meals/AddIngredientDialog.vue';

const props = defineProps<{
  meal: MealDTO;
  userId: Uuid;
}>();

const router = useRouter();
const { data, refetch } = useGetIngredients();

const allIngredients = computed(() => data.value?.data ?? []);

const { pickedIngredients, isModified, isPicked, toggleIngredientPick } = useIngredientEdit(
  props.meal.ingredients,
);

async function editExistingMeal() {
  return recreateMeal({
    ...props.meal,
    ingredients: pickedIngredients.value,
  });
}

async function editIngredientlessMeal() {
  return addIngredientsToMeal(props.meal.mealId, pickedIngredients.value, props.meal.restaurantId);
}

// Since meals are immutable, we need to recreate the meal with the new ingredients
async function handleEditMeal() {
  if (props.meal.ingredients.length === 0) {
    await editIngredientlessMeal();
    location.reload();
    return;
  }

  const res = await editExistingMeal();

  if (res.status === 200) {
    await router.replace(`/manage/meals/edit/${res.data.mealId}`);
    // This always guarantees that operations are performed against the updated meal
    location.reload();
  } else {
    toast.error('Failed to update meal');
    console.error('recreateMeal:postMeals', res);
  }
}

async function handleAddNewIngredient(ingredient: IngredientInputDTO) {
  const res = await postIngredients(ingredient, {
    mealId: props.meal.mealId,
  });

  if (res.status === 200) {
    await refetch();
  }
}
</script>

<template>
  <div class="max-w-screen-md">
    <header class="flex justify-between mb-10">
      <h1 class="text-2xl font-semibold tracking-tight">Edit meal ingredients</h1>

      <Button @click="handleEditMeal" :disabled="!isModified" class="transition-opacity">
        <SaveIcon class="inline-block w-4 h-4 mr-2" /> Save
      </Button>
    </header>

    <article class="bg-white border border-neutral-200 p-4 rounded-md">
      <section class="mb-10">
        <h2 class="text-xl font-semibold mb-3">Meal details</h2>
        <div class="flex gap-1 mb-2">
          <p><span class="font-bold">Name:</span> {{ meal.name }}</p>
        </div>
        <div class="flex gap-1">
          <p><span class="font-bold">Description:</span> {{ meal.description }}</p>
        </div>
      </section>

      <section>
        <h2 class="text-xl font-semibold mb-3">Ingredients</h2>

        <ul class="space-y-2 mb-4">
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
                <CircleCheckBigIcon class="h-5 w-5" v-if="isPicked(ingredient.ingredientId)" />
                <PlusIcon class="h-5 w-5" v-else />
              </Button>

              <p>
                {{ ingredient.name }}
              </p>
            </div>
          </li>
        </ul>

        <AddIngredientDialog @add-ingredient="handleAddNewIngredient" />
      </section>
    </article>
  </div>
</template>
