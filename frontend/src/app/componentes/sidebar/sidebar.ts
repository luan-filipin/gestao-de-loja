import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  collapsed = signal(false);
  productOpen = signal(false);

  toggleSidebar(): void {
    this.collapsed.update((value) => !value);
  }

  toggleProduct(): void {
    if (this.collapsed()) {
      this.collapsed.set(false);
      this.productOpen.set(true);
      return;
    }

    this.productOpen.update((value) => !value);
  }
}
