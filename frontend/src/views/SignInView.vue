<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod';
import { useForm } from 'vee-validate';
import * as z from 'zod';

import {
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

const formSchema = toTypedSchema(
  z.object({
    username: z.string().min(3),
    password: z.string().min(6),
  }),
);

const form = useForm({
  validationSchema: formSchema,
});

const onSubmit = form.handleSubmit((values) => {
  console.log(values);
});

const isValid = !form.meta.value.valid;
</script>

<template>
  <div class="border border-neutral-900 grid items-center flex-grow">
    <div class="w-80 mx-auto border border-neutral-800 p-8 rounded">
      <form @submit="onSubmit" class="space-y-4">
        <h1 class="text-xl text-center font-bold">Sign-In</h1>
        <FormField v-slot="{ componentField }" name="email">
          <FormItem>
            <FormLabel>Username</FormLabel>
            <FormControl>
              <Input type="text" placeholder="Username" v-bind="componentField" />
            </FormControl>
            <FormDescription class="sr-only">This is your Username. </FormDescription>
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="password">
          <FormItem>
            <FormLabel>Password</FormLabel>
            <FormControl>
              <Input type="password" placeholder="Password" v-bind="componentField" />
            </FormControl>
            <FormDescription class="sr-only"> This is your password. </FormDescription>
            <FormMessage />
          </FormItem>
        </FormField>

        <Button :disabled="isValid" type="submit" class="mt-8 w-full"> Submit </Button>
      </form>
    </div>
  </div>
</template>
