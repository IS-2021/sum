<script setup lang="ts">
import { ref } from 'vue';

import Input from '@/components/ui/input/Input.vue';
import Button from '@/components/ui/button/Button.vue';
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { toast } from 'vue-sonner';

import { postMeals } from '@/lib/api/meals/meals';
import type { Uuid } from '@/lib/api-model';
import { useRouter } from 'vue-router';

import { useForm } from 'vee-validate';
import { toTypedSchema } from '@vee-validate/zod';
import * as z from 'zod';

import type { ValidationFailed422Response } from '@/lib/api-model';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { AlertCircleIcon } from 'lucide-vue-next';

const formSchema = toTypedSchema(
  z.object({
    description: z.string().min(2).max(50),
    name: z.string().min(2).max(50),
  }),
);

const form = useForm({
  validationSchema: formSchema,
});

const errorMessage = ref('');

const props = defineProps<{
  userId: Uuid;
}>();

const router = useRouter();

const addNewMeal = form.handleSubmit(async (values) => {
  const response = await postMeals({
    description: values.description,
    name: values.name,
    restaurantId: props.userId,
  });

  if (response.status === 200) {
    toast.success('Meal added successfully!');
    await router.push(`/manage/meals/edit/${response.data.mealId}`);
  } else if (response.status === 401) {
    const { message } = response.data as unknown as ValidationFailed422Response;

    errorMessage.value = message;
  } else if (response.status === 404) {
    errorMessage.value = 'Unable to add meal';
  }
});

const isValid = form.meta.value.valid;
</script>

<template>
  <h1 class="text-2xl font-semibold tracking-tight mb-6">Add Meal</h1>
  <p class="mb-16">After you add your meal you will be willing to add ingredients to it</p>

  <Alert variant="destructive" v-if="errorMessage" class="mb-4">
    <AlertCircleIcon class="h-4 w-4" />
    <AlertTitle>There's an error</AlertTitle>
    <AlertDescription>
      {{ errorMessage }}
    </AlertDescription>
  </Alert>

  <form class="w-2/3 space-y-6" @submit="addNewMeal">
    <FormField v-slot="{ componentField }" name="name">
      <FormItem>
        <FormLabel>Name</FormLabel>
        <FormControl>
          <Input type="text" placeholder="meal name..." v-bind="componentField" />
        </FormControl>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="description">
      <FormItem>
        <FormLabel>Description</FormLabel>
        <FormControl>
          <Input type="text" placeholder="meal description..." v-bind="componentField" />
        </FormControl>
        <FormMessage />
      </FormItem>
    </FormField>
    <Button :disabled="!isValid" type="submit"> Add meal </Button>
  </form>
</template>
