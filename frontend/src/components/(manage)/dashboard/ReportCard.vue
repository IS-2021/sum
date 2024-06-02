<script setup lang="ts">
import type { ReportDTO, Uuid } from '@/lib/api-model';
import { BanIcon, CalendarPlusIcon, ThumbsDownIcon, UserRoundIcon, XIcon } from 'lucide-vue-next';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';
import ConfirmDialog from '@/components/dialogs/ConfirmDialog.vue';
import { formatDateShort } from '@/lib/formatters';

type ReportCardActions =
  | 'banUser'
  | 'banRestaurant'
  | 'closeReport'
  | 'viewUser'
  | 'viewRestaurant';

interface ReportCardProps {
  report: ReportDTO;
  showActionButtons?: boolean;
  actions?: ReportCardActions[];
}

defineProps<ReportCardProps>();

const emits = defineEmits<{
  (e: 'banUser', reportId: Uuid): void;
  (e: 'banRestaurant', reportId: Uuid): void;
  (e: 'closeReport', reportId: Uuid): void;
}>();
</script>

<template>
  <article class="rounded border border-neutral-300 bg-white px-6 py-4">
    <div class="mb-4 flex items-center gap-6">
      <ThumbsDownIcon class="flex-shrink-0 text-red-500" />
      <p class="max-w-prose flex-shrink">{{ report.cause }}</p>
    </div>
    <p class="flex items-center text-right text-sm text-neutral-500">
      <CalendarPlusIcon class="mr-2 size-4" /> {{ formatDateShort(report.timestamp) }}
    </p>

    <template v-if="showActionButtons">
      <Separator class="my-4" />

      <div class="flex flex-wrap gap-2">
        <ConfirmDialog v-if="actions?.includes('banUser')">
          <template v-slot:trigger>
            <Button variant="outline-destructive">
              <BanIcon class="mr-2 inline-block h-4 w-4" />Ban user
            </Button>
          </template>
          <template v-slot:description> Are you sure you want to ban this user? </template>
          <template v-slot:confirmButton>
            <Button @click="emits('banUser', report.id)" type="submit" variant="destructive">
              Confirm
            </Button>
          </template>
        </ConfirmDialog>

        <ConfirmDialog v-if="actions?.includes('banRestaurant')">
          <template v-slot:trigger>
            <Button variant="outline-destructive">
              <BanIcon class="mr-2 inline-block h-4 w-4" />Ban restaurant
            </Button>
          </template>
          <template v-slot:description> Are you sure you want to ban this restaurant? </template>
          <template v-slot:confirmButton>
            <Button @click="emits('banRestaurant', report.id)" type="submit" variant="destructive">
              Confirm
            </Button>
          </template>
        </ConfirmDialog>

        <Button
          v-if="actions?.includes('closeReport')"
          variant="secondary"
          @click="emits('closeReport', report.id)"
        >
          <XIcon class="mr-2 inline-block h-4 w-4" /> Close report
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
