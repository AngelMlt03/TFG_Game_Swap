import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';

@NgModule({
  declarations: [],
  imports: [BrowserModule],
  providers: [provideHttpClient()],
  bootstrap: []
})
export class AppModule {}
