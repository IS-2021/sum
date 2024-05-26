<script setup lang="ts">
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { LogOut, MapPinIcon, UserIcon } from 'lucide-vue-next';
import { useUser } from '@/composables/useUser';
import { useRouter } from 'vue-router/auto';

const router = useRouter();
const { signOut } = useUser();

function onChangeLocationSelect() {
  router.push('/settings');
}
</script>

<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child class="bg-opacity-0 hover:bg-opacity-15 hover:text-white">
      <Button variant="ghost"> <UserIcon class="h-4 w-4 mr-3" /> john.cena </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent class="w-56">
      <DropdownMenuLabel>Your Profile</DropdownMenuLabel>
      <DropdownMenuItem disabled>Location: Łódź</DropdownMenuItem>

      <DropdownMenuSeparator />

      <DropdownMenuItem @select="onChangeLocationSelect">
        <MapPinIcon class="mr-2 h-4 w-4" />
        <span>Change location</span>
      </DropdownMenuItem>

      <DropdownMenuSeparator />

      <DropdownMenuItem @select="signOut">
        <LogOut class="mr-2 h-4 w-4" />
        <span>Sign out</span>
      </DropdownMenuItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>
