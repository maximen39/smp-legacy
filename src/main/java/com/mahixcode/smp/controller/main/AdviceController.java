/*
 * Copyright (C) 2019 maximen39
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahixcode.smp.controller.main;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author maximen39
 */
@ControllerAdvice(basePackages = "ru.maximen.luckyverse.cms.controller.main")
public class AdviceController {

    /*  private final SocketHandler socketHandler;
      private final Store<String, Optional<PassionProfile>> profileStore;
      private final Config config;

      @Autowired
      public AdviceController(SocketHandler socketHandler, Store<String, Optional<PassionProfile>> profileStore,
                              Config config) {
          this.socketHandler = socketHandler;
          this.profileStore = profileStore;
          this.config = config;
      }

      @ModelAttribute("sitename")
      public String getSitename() {
          return config.siteName;
      }

      @ModelAttribute("recaptchaKey")
      public String getRecaptchaKey() {
          return config.recaptchaKey;
      }
  */
/*    @ModelAttribute("username")
    public String getUsername() {
        Optional<User> optionalUser = Utils.getUser();
        if (!optionalUser.isPresent()) {
            return "Гость";
        }
        return optionalUser.get().getUsername();
    }*/

/*    @ModelAttribute("balance")
    public String getBalance() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return "0";
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(passionProfile1 -> String.valueOf(passionProfile1.getBalance())).orElse("0");
    }

    @ModelAttribute("payouts")
    public List<Payout> getPayouts() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return Collections.emptyList();
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        Comparator<Payout> payoutComparator = Comparator.comparingLong(payout -> payout.getCreateAt().getTime());
        return passionProfile.map(passionProfile1 -> passionProfile1
                .getPayouts()
                .values()
                .stream()
                .sorted(payoutComparator.reversed())
                .collect(toList()))
                .orElse(Collections.emptyList());
    }

    @ModelAttribute("referralCode")
    public String getReferralCode() {
        return getUsername();
    }

    @ModelAttribute("referralsEarned")
    public double getReferralsEarned() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return 0.0;
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getReferralsEarned).orElse(0.0);
    }

    @ModelAttribute("vkId")
    public Long getVkId() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return null;
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getVkId).orElse(null);
    }

    @ModelAttribute("invited")
    public String getInvited() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return null;
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getInvited).orElse(null);
    }

    @ModelAttribute("referralsCount")
    public double getReferralsCount() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return 0.0;
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getReferralsCount).orElse(0.0);
    }

    @ModelAttribute("referrals")
    public List<Referral> getReferrals() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return Collections.emptyList();
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getReferrals).orElse(Collections.emptyList());
    }

    @ModelAttribute("payments")
    public List<Payment> getPayments() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return Collections.emptyList();
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getPayments).orElse(Collections.emptyList());
    }

    @ModelAttribute("email")
    public String getEmail() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return "Мыло упало.";
        }
        SessionUser sessionUser = optionalSessionUser.get();
        Optional<PassionProfile> passionProfile = profileStore.fetch(sessionUser.getUsername());
        return passionProfile.map(PassionProfile::getEmail).orElse("Мыло упало.");
    }

    @ModelAttribute("hash")
    public String getHash() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return "тип хеш";
        }
        SessionUser sessionUser = optionalSessionUser.get();
        return sessionUser.encryptedNumber().getHash();
    }

    @ModelAttribute("online")
    public int getOnline() {
        return socketHandler.webSocketSessions().size();
    }

    @ModelAttribute("symbol")
    public char getSymbol() {
        return config.symbol.charAt(0);
    }

    @ModelAttribute("title")
    public String getTitle() {
        return config.title;
    }

    @ModelAttribute("description")
    public String getDescription() {
        return config.description;
    }

    @ModelAttribute("author")
    public String getAuthor() {
        return config.author;
    }

    @ModelAttribute("footer")
    public String getFooter() {
        return config.footer;
    }

    @ModelAttribute("vkGroupName")
    public String getVkGroupName() {
        return config.vkGroupName;
    }

    @ModelAttribute("keywords")
    public String getKeywords() {
        return config.keywords;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Optional<SessionUser> optionalSessionUser = Utils.getSessionUser();
        if (!optionalSessionUser.isPresent()) {
            return false;
        }
        SessionUser sessionUser = optionalSessionUser.get();
        return sessionUser
                .getAuthorities()
                .stream()
                .anyMatch(authority -> Permission.fromString(authority.getAuthority()) == Permission.ADMIN);
    }
*/
/*    @ModelAttribute("isAuth")
    public boolean isAuth() {
        return Utils.isAuth();
    }*/
}
