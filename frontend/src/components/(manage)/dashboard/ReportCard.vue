<script setup lang="ts">
import type { ReportDTO, Uuid } from '@/lib/api-model';
import { BanIcon, ThumbsDownIcon, UserRoundIcon } from 'lucide-vue-next';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';

type ReportCardActions = 'banUser' | 'banRestaurant' | 'viewUser' | 'viewRestaurant';

interface ReportCardProps {
  report: ReportDTO;
  showActionButtons: boolean;
  actions?: ReportCardActions[];
}

defineProps<ReportCardProps>();

const emits = defineEmits<{
  (e: 'banUser', userId: Uuid): void;
  (e: 'banRestaurant', restaurantId: Uuid): void;
}>();
</script>

<template>
  <article class="rounded border border-neutral-300 bg-white px-6 py-4">
    <div class="flex items-center gap-6">
      <ThumbsDownIcon class="flex-shrink-0 text-red-500" />
      <p class="max-w-prose flex-shrink">{{ report.cause }}</p>
    </div>

    <template v-if="showActionButtons">
      <Separator class="my-4" />

      <div class="flex gap-2">
        <Button
          v-if="actions?.includes('banUser')"
          @click="emits('banUser', report.userId)"
          variant="outline-destructive"
        >
          <BanIcon class="mr-2 inline-block h-4 w-4" />Ban user
        </Button>
        <Button
          v-if="actions?.includes('banRestaurant')"
          @click="emits('banRestaurant', report.restaurantId)"
          variant="outline-destructive"
        >
          <BanIcon class="mr-2 inline-block h-4 w-4" />Ban restaurant
        </Button>

        <Button v-if="actions?.includes('viewUser')" variant="secondary" as-child>
          <RouterLink :to="`/admin/users/${report.userId}`">
            <UserRoundIcon class="mr-2 inline-block h-4 w-4" />View user
          </RouterLink>
        </Button>
        <Button v-if="actions?.includes('viewRestaurant')" variant="secondary" as-child>
          <RouterLink :to="`/admin/restaurants/${report.restaurantId}`">
            <UserRoundIcon class="mr-2 inline-block h-4 w-4" />View restaurant
          </RouterLink>
        </Button>
      </div>
    </template>
  </article>
</template>
