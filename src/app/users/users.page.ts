import {UserInfo} from '../user/info/user.info';

export class UsersPage {
  page: number;
  size: number;
  totalPage: number;
  totalElements: number;
  elements: {
    links: [
      {
        rel: string,
        href: string
      },
      {
        rel: string,
        href: string
      },
      {
        rel: string,
        href: string
      }
    ],
    content: Array<UserInfo>
  };
}
