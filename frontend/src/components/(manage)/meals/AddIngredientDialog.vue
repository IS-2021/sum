<script setup lang="ts">
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { toTypedSchema } from '@vee-validate/zod';
import { z } from 'zod';
import { useForm } from 'vee-validate';
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';

const emits = defineEmits<{
  (e: 'addIngredient', ingredient: FormSchema): void;
}>();

const schema = z.object({
  name: z.string(),
  type: z.string(),
});
type FormSchema = z.infer<typeof schema>;

const formSchema = toTypedSchema(schema);
const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    name: '',
    type: '',
  },
});

const handleSubmit = form.handleSubmit((formData) => {
  emits('addIngredient', formData);
});
</script>

<template>
  <Dialog>
    <DialogTrigger as-child>
      <Button variant="secondary"> Add new ingredient </Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Add new ingredient</DialogTitle>
        <DialogDescription>
          Ingredient was from the list missing? No problem! Add it here.
        </DialogDescription>
      </DialogHeader>

      <form @submit="handleSubmit" class="space-y-4">
        <FormField v-slot="{ componentField }" name="name">
          <FormItem class="flex-grow">
            <FormLabel>Name</FormLabel>
            <FormControl>
              <Input type="text" placeholder="Chanterelle" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>
        <FormField v-slot="{ componentField }" name="type">
          <FormItem class="flex-grow">
            <FormLabel>Type</FormLabel>
            <FormControl>
              <Input type="text" placeholder="Mushroom" v-bind="componentField" />
            </FormControl>
            <FormMessage />
          </FormItem>
        </FormField>

        <DialogFooter>
          <DialogClose as-child>
            <Button type="submit" :disabled="!form.meta.value.valid"> Add ingredient </Button>
          </DialogClose>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>
