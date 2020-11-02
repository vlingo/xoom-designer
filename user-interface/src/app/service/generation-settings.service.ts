import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { GenerationSettings } from '../model/generation-settings';

@Injectable()
export class GenerationSettingsService {

  constructor(private http: HttpClient) {

  }

  generate(settings : GenerationSettings) : Observable<any> {
    return this.http.post<any>(environment.endpoint, settings);
  }
}
