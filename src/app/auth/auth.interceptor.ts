import {HTTP_INTERCEPTORS, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {TokenStorageService} from './token-storage.service';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private tokenStorage: TokenStorageService) {
  }

  public intercept(req: HttpRequest<any>, next: HttpHandler): any {
    const token = this.tokenStorage.getToken();
    const authReq = token ? req.clone({headers: req.headers.set(TOKEN_HEADER_KEY, this.tokenStorage.getToken())}) : req;
    console.log('intercept:', next.handle(authReq));
    return next.handle(authReq);
  }
}

export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
];
