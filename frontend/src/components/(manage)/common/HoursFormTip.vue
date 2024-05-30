<script setup lang="ts">
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert';
import { ClockIcon } from 'lucide-vue-next';
</script>

<template>
  <Alert class="mb-3 bg-secondary/25">
    <ClockIcon class="h-4 w-4" />
    <AlertTitle>How to fill the form?</AlertTitle>
    <AlertDescription>
      <ul class="ml-4 list-disc">
        <li>Use HH:MM 24-hour format,</li>
        <li>If restaurant is closed on a given day, leave both fields empty.</li>
      </ul>
    </AlertDescription>
  </Alert>
</template>
