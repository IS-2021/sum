<script setup lang="ts">
import type { Timestamp } from '@/lib/api-model';
import { ThumbsDownIcon } from 'lucide-vue-next';

interface ReportCardProps {
  cause: string;
  reportedAt: Timestamp;
}

const props = defineProps<ReportCardProps>();
</script>

<template>
  <div class="flex px-6 gap-6 py-4 bg-white rounded border border-neutral-300 items-center">
    <ThumbsDownIcon class="text-red-500 flex-shrink-0" />
    <p class="flex-shrink max-w-prose">{{ cause }}</p>
  </div>
</template>
