import { User } from "../entity/abstract-user";

export interface IUserRepository {
  getUserByUsernameAndPassword(userName: String, password: String): User;
  deleteUserByUserNameAndEmail(userName: String, email: String): boolean;

  create(userToCreate: User): Promise<User>;
  update(userToUpdate: User): Promise<User>;
}
