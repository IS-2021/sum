<script setup lang="ts">
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
import { toTypedSchema } from '@vee-validate/zod';
import * as z from 'zod';
import { useForm } from 'vee-validate';
import { postLogin } from '@/lib/api/auth/auth';
import { ref } from 'vue';
import type { ValidationFailed422Response } from '@/lib/api-model';
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { AlertCircleIcon } from 'lucide-vue-next';
import { useRouter } from 'vue-router/auto';

const router = useRouter();

const formSchema = toTypedSchema(
  z.object({
    username: z.string().min(3),
    password: z.string().min(6),
  }),
);

const form = useForm({
  validationSchema: formSchema,
});
const errorMessage = ref('');

const onSubmit = form.handleSubmit(async (credentials) => {
  const res = await postLogin(credentials, {
    validateStatus: (status) => status < 500,
  });

  if (res.status === 200) {
    errorMessage.value = '';
    await router.push('/');
  } else if (res.status === 400) {
    const { message } = res.data as unknown as ValidationFailed422Response;

    errorMessage.value = message;
  }
});

const isValid = form.meta.value.valid;
</script>

<template>
  <Alert variant="destructive" v-if="errorMessage" class="mb-4">
    <AlertCircleIcon class="h-4 w-4" />
    <AlertTitle>There's an error</AlertTitle>
    <AlertDescription>
      {{ errorMessage }}
    </AlertDescription>
  </Alert>

  <form @submit="onSubmit" class="space-y-4">
    <FormField v-slot="{ componentField }" name="username">
      <FormItem>
        <FormLabel>Username</FormLabel>
        <FormControl>
          <Input type="text" placeholder="Username" v-bind="componentField" />
        </FormControl>
        <FormDescription class="sr-only">This is your Username.</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>
    <FormField v-slot="{ componentField }" name="password">
      <FormItem>
        <FormLabel>Password</FormLabel>
        <FormControl>
          <Input type="password" placeholder="Password" v-bind="componentField" />
        </FormControl>
        <FormDescription class="sr-only"> This is your password.</FormDescription>
        <FormMessage />
      </FormItem>
    </FormField>

    <Button :disabled="!isValid" type="submit" class="mt-8 w-full"> Submit</Button>
  </form>
</template>
