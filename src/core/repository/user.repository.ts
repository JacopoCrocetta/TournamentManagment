import { User } from "../entity/abstract-user";

export interface IUserRepository {
  getUserByUsernameAndPassword(userName: String, password: String): User;
  deleteUserByUserNameAndPassword(userName: String, password: String): boolean;

  create(matchToCreate: User): Promise<User>;
  update(matchToUpdate: User): Promise<User>;
}
